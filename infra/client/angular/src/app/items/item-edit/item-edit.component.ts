import {Component, OnDestroy, OnInit} from '@angular/core';
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
import {Subject} from 'rxjs';
import {distinctUntilChanged, takeUntil} from 'rxjs/operators';

@Component({
  selector: 'app-item-edit',
  templateUrl: './item-edit.component.html',
  styleUrls: ['./item-edit.component.scss']
})
export class ItemEditComponent implements OnInit, OnDestroy {

  createMode: boolean;
  item: Item;
  manufacturers: Array<Manufacturer>;
  isScalarValue = isScalarValue;
  isDictionaryValue = isDictionaryValue;
  formGroup: FormGroup;

  private valuesFormGroup: FormGroup;
  private nameFormControl: FormControl;
  private manufacturerFormControl: FormControl;
  private manufacturersCodeFormControl: FormControl;
  private readonly unsubscribe = new Subject();

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
    this.manufacturersCodeFormControl = new FormControl({
      value: this.item.manufacturersCode,
      disabled: this.item.manufacturer == null
    });
    this.valuesFormGroup = new FormGroup(
      Object.assign({},
        ...this.item.values.map(value => ({
          [value.name]: new FormControl(value.value, [])
        }))));
    this.formGroup = new FormGroup(
      {
        itemName: this.nameFormControl,
        manufacturer: this.manufacturerFormControl,
        manufacturersCode: this.manufacturersCodeFormControl,
        values: this.valuesFormGroup
      });

    // change manufacturers code disabled state depending on manufacturer's selection
    this.manufacturerFormControl.valueChanges.pipe(
      takeUntil(this.unsubscribe),
      distinctUntilChanged()
    ).subscribe(value => {
      if (value == null || (typeof value === 'string' && value.trim().length === 0)) {
        this.manufacturersCodeFormControl.disable();
      } else {
        if (this.manufacturersCodeFormControl.disabled) {
          this.manufacturersCodeFormControl.enable();
        }
      }
    });
  }

  ngOnDestroy(): void {
    this.unsubscribe.next();
    this.unsubscribe.complete();
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
      const manufacturerValue = this.manufacturerFormControl.value;
      if (manufacturerValue == null || (typeof manufacturerValue === 'string' && manufacturerValue.trim().length === 0)) {
        this.item.manufacturer = null;
      } else if (typeof manufacturerValue === 'string') {
        this.item.manufacturer = {
          id: null,
          name: manufacturerValue.trim()
        };
      } else {
        this.item.manufacturer = this.manufacturerFormControl.value;
      }
    }
    if (this.manufacturersCodeFormControl.dirty) {
      this.item.manufacturersCode = this.manufacturersCodeFormControl.value;
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
        // TODO handle manufacturer store
        this.router.navigate(['items', this.item.name]).catch(reason => console.warn(reason));
      });
    } else if (this.formGroup.dirty) {
      this.itemService.updateItem(this.item.name, this.item.manufacturer, this.item.manufacturersCode, changes).subscribe(response => {
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
