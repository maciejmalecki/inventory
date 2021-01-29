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

  private valuesFormGroup: FormGroup;
  private nameFormControl: FormControl;
  private manufacturerFormControl: FormControl;

  constructor(
    private readonly activatedRoute: ActivatedRoute,
    private readonly router: Router,
    private readonly itemService: ItemService
  ) {
    this.item = activatedRoute.snapshot.data.item;
    this.manufacturers = activatedRoute.snapshot.data.manufacturers;
    this.createMode = activatedRoute.snapshot.data.createMode;
  }

  ngOnInit(): void {
    this.nameFormControl = new FormControl({
      value: this.item.name,
      disabled: !this.createMode
    });
    this.manufacturerFormControl = new FormControl(this.item.manufacturer);
    this.valuesFormGroup = new FormGroup(
      Object.assign({},
        ...this.item.values.map(value => ({
          [value.name]: new FormControl(value.value, [])
        }))));
    this.formGroup = new FormGroup(
      {
        itemName: this.nameFormControl,
        manufacturer: this.manufacturerFormControl,
        values: this.valuesFormGroup
      });
  }

  manufacturerDisplayFn = (manufacturer: Manufacturer) => manufacturer ? manufacturer.name : '';

  save(): void {
    const changes: Array<AttributeValuation> = [];

    if (this.formGroup.invalid) {
      console.warn('An attempt to save invalid data.');
      return;
    }

    if (this.createMode) {
      this.item.name = this.nameFormControl.value;
    }
    if (this.manufacturerFormControl.dirty) {
      this.item.manufacturer = this.manufacturerFormControl.value;
    }

    for (const prop in this.valuesFormGroup.controls) {
      if (Object.prototype.hasOwnProperty.call(this.valuesFormGroup.controls, prop)) {
        const formControl = this.valuesFormGroup.get(prop);
        if (this.createMode || formControl.dirty) {
          changes.push({
            attribute: prop,
            value: formControl.value
          });
        }
      }
    }

    if (this.createMode) {
      this.itemService.createItem(this.item.name, this.item.itemClassId.id, this.item.itemClassId.version, changes).subscribe(_ => {
        this.router.navigate(['items', this.item.name]).catch(reason => console.warn(reason));
      });
    } else if (this.formGroup.dirty) {
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
