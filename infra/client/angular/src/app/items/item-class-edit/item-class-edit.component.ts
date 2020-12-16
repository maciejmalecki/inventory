import { Component, OnInit } from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {isDictionaryType, isScalarType, ItemClass} from '../../shared/services/item-class.service';
import {FormControl, FormGroup, MaxLengthValidator} from '@angular/forms';

@Component({
  selector: 'app-item-class-edit',
  templateUrl: './item-class-edit.component.html',
  styleUrls: ['./item-class-edit.component.scss']
})
export class ItemClassEditComponent implements OnInit {

  itemClass: ItemClass;
  isScalarType = isScalarType;
  isDictionaryType = isDictionaryType;
  formGroup: FormGroup;

  constructor(private readonly route: ActivatedRoute) {
    this.itemClass = route.snapshot.data.itemClass;
  }

  ngOnInit(): void {
    this.formGroup = new FormGroup({
      description: new FormControl(this.itemClass.description, [])
    });
  }
}
