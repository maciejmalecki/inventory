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
import {Manufacturer} from '../../shared/services/manufacturer.service';

@Component({
  selector: 'app-item-edit',
  templateUrl: './item-edit.component.html',
  styleUrls: ['./item-edit.component.scss']
})
export class ItemEditComponent implements OnInit {

  createMode: boolean;
  item: Item;
  manufacturers: Array<Manufacturer>;
  isScalarValue = isScalarValue;
  isDictionaryValue = isDictionaryValue;
  formGroup: FormGroup;

  constructor(
    private readonly activatedRoute: ActivatedRoute,
    private readonly router: Router,
    private readonly itemService: ItemService
  ) {
    this.item = activatedRoute.snapshot.data.item;
    this.manufacturers = activatedRoute.snapshot.data.manufacturers;
    this.createMode = activatedRoute.snapshot.data.createMode;

    console.log(this.item);
  }

  ngOnInit(): void {
    this.formGroup = new FormGroup(
      {
        itemName: new FormControl({
          value: this.item.name,
          disabled: !this.createMode
        }),
        manufacturer: new FormControl(this.item.manufacturer),
        values: new FormGroup(
          Object.assign({},
            ...this.item.values.map(value => ({
              [value.name]: new FormControl(value.value, [])
            }))))
      });
  }

  manufacturerDisplayFn = (manufacturer: Manufacturer) => manufacturer ? manufacturer.name : '';

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
      this.itemService.createItem(this.item.name, this.item.itemClassId.id, this.item.itemClassId.version, changes).subscribe(_ => {
        this.router.navigate(['items', this.item.name]).catch(reason => console.warn(reason));
      });
    } else if (changes.length > 0) {
      this.itemService.updateItem(this.item.name, this.item.manufacturer, changes).subscribe(response => {
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
