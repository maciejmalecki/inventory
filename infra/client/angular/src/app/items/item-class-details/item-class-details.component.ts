import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {isDictionaryAttribute, isScalarAttribute, ItemClass} from '../../shared/services/item-class.service';

@Component({
  selector: 'app-item-class-details',
  templateUrl: './item-class-details.component.html',
  styleUrls: ['./item-class-details.component.scss']
})
export class ItemClassDetailsComponent implements OnInit {

  itemClass: ItemClass;
  isScalar = isScalarAttribute;
  isDictionary = isDictionaryAttribute;

  constructor(private readonly route: ActivatedRoute) {
    this.itemClass = route.snapshot.data.itemClass;
  }

  ngOnInit(): void {
  }
}
