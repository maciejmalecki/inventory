import {Component, OnInit} from '@angular/core';
import {isDictionaryValue, isScalarValue, Item} from '../../shared/services/item.service';
import {ActivatedRoute} from '@angular/router';
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

  constructor(private readonly activatedRoute: ActivatedRoute) {
    this.item = activatedRoute.snapshot.data.item;
  }

  ngOnInit(): void {
    this.formGroup = new FormGroup(
      Object.assign({}, ...this.item.values.map(value => ({
        [value.attribute.name]: new FormControl(value.value)
      })))
    );
  }
}
