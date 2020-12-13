import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {AttributeType, isDictionaryType, isScalarType, ItemClass} from "../../shared/services/item-class.service";

@Component({
  selector: 'app-item-class-details',
  templateUrl: './item-class-details.component.html',
  styleUrls: ['./item-class-details.component.scss']
})
export class ItemClassDetailsComponent implements OnInit {

  itemClass: ItemClass;
  isScalarType = isScalarType;
  isDictionaryType = isDictionaryType;

  constructor(private readonly route: ActivatedRoute) {
    this.itemClass = route.snapshot.data.itemClass;
  }

  ngOnInit(): void {
  }
}
