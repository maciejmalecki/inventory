import {Component, OnInit} from '@angular/core';
import {Observable} from 'rxjs';
import {ItemClassHeader, ItemClassService} from '../../shared/services/item-class.service';
import {FormControl, FormGroup} from '@angular/forms';

@Component({
  selector: 'app-item-classes',
  templateUrl: './item-classes.component.html',
  styleUrls: ['./item-classes.component.scss']
})
export class ItemClassesComponent implements OnInit {

  itemClasses$: Observable<Array<ItemClassHeader>>;

  formGroup: FormGroup;

  constructor(
    private readonly itemClassService: ItemClassService) {
  }

  ngOnInit(): void {
    this.itemClasses$ = this.itemClassService.getAllItemClasses();
    this.formGroup = new FormGroup({
      search: new FormControl('')
    });
  }

}
