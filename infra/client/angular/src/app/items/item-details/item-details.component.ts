import { Component, OnInit } from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Item} from '../../shared/services/item.service';
import {isDictionaryType, isScalarType} from '../../shared/services/item-class.service';

@Component({
  selector: 'app-item-details',
  templateUrl: './item-details.component.html',
  styleUrls: ['./item-details.component.scss']
})
export class ItemDetailsComponent implements OnInit {

  item: Item;
  isScalarType = isScalarType;
  isDictionaryType = isDictionaryType;

  constructor(private readonly activatedRoute: ActivatedRoute) {
    this.item = activatedRoute.snapshot.data.item;
  }

  ngOnInit(): void {
  }

}
