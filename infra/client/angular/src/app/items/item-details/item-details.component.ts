import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {DictionaryValue, isDictionaryValue, isScalarValue, Item, Value} from '../../shared/services/item.service';
import {DictionaryType} from '../../shared/services/item-class.service';

@Component({
  selector: 'app-item-details',
  templateUrl: './item-details.component.html',
  styleUrls: ['./item-details.component.scss']
})
export class ItemDetailsComponent implements OnInit {

  item: Item;
  isScalarValue = isScalarValue;
  isDictionaryValue = isDictionaryValue;

  constructor(private readonly activatedRoute: ActivatedRoute) {
    this.item = activatedRoute.snapshot.data.item;
  }

  ngOnInit(): void {
  }

  getDictionaryValue(value: DictionaryValue) {
    return (value.attribute.type as DictionaryType).items.find(value1 => value1.code === value.value).value;
  }
}
