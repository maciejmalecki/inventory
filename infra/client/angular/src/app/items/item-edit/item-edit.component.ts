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
  }

  ngOnInit(): void {
    this.formGroup = new FormGroup(
      Object.assign({}, ...this.item.values.map(value => ({
        [value.attribute.name]: new FormControl(value.value)
      })))
    );
  }

  save(): void {
    const controls = this.formGroup.controls;
    const changes: Array<AttributeValuation> = [];
    if (this.formGroup.dirty) {
      for (const prop in controls) {
        if (Object.prototype.hasOwnProperty.call(controls, prop)) {
          const formControl = this.formGroup.get(prop);
          if (formControl.dirty) {
            changes.push({
              attribute: prop,
              value: formControl.value
            });
          }
        }
      }
    }
    if (changes.length > 0) {
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
