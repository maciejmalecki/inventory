import {Component, OnInit} from '@angular/core';
import {
  AttributeValuation,
  isDictionaryValue,
  isScalarValue,
  Item,
  ItemService
} from '../../shared/services/item.service';
import {ActivatedRoute, Router} from '@angular/router';
import {FormControl, FormGroup} from '@angular/forms';

@Component({
  selector: 'app-item-edit',
  templateUrl: './item-edit.component.html',
  styleUrls: ['./item-edit.component.scss']
})
export class ItemEditComponent implements OnInit {

  createMode: boolean;
  item: Item;
  isScalarValue = isScalarValue;
  isDictionaryValue = isDictionaryValue;
  formGroup: FormGroup;

  constructor(
    private readonly activatedRoute: ActivatedRoute,
    private readonly router: Router,
    private readonly itemService: ItemService
  ) {
    this.item = activatedRoute.snapshot.data.item;
    this.createMode = activatedRoute.snapshot.data.createMode;
  }

  ngOnInit(): void {
    this.formGroup = new FormGroup(
      Object.assign({
        itemName: new FormControl({
          value: this.item.name,
          disabled: !this.createMode
        })
      }, ...this.item.values.map(value => ({
        [value.attribute.name]: new FormControl(value.data, [])
      })))
    );
  }

  save(): void {
    const controls = this.formGroup.controls;
    const changes: Array<AttributeValuation> = [];
    if (this.formGroup.invalid) {
      console.warn('An attempt to save invalid data.');
      return;
    }
    if (this.createMode || this.formGroup.dirty) {
      const nameFC = this.formGroup.get('itemName');
      if (this.createMode) {
        this.item.name = nameFC.value;
      }
      for (const prop in controls) {
        if (Object.prototype.hasOwnProperty.call(controls, prop)) {
          const formControl = this.formGroup.get(prop);
          if (this.createMode || formControl.dirty) {
            changes.push({
              attribute: prop,
              value: formControl.value
            });
          }
        }
      }
    }
    if (this.createMode) {
      this.itemService.createItem(this.item.name, this.item.itemClassId.id, this.item.itemClassId.version, changes).subscribe(response => {
        this.router.navigate(['items', this.item.name]).catch(reason => console.warn(reason));
      });
    }
    else if (changes.length > 0) {
      this.itemService.updateItem(this.item.name, changes).subscribe(response => {
        if (response.ok) {
          this.router.navigate(['items', this.item.name]).catch(reason => console.warn(reason));
        } else {
          console.error(response);
        }
      }, error => {
        console.error(error);
      });
    }
  }
}
