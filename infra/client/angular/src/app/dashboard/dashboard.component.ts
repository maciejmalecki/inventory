import {Component, OnInit} from '@angular/core';
import {ItemClassHeader, ItemClassService} from '../shared/services/item-class.service';
import {Observable} from 'rxjs';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit {

  itemClasses$: Observable<Array<ItemClassHeader>>;

  constructor(
    private readonly itemClassService: ItemClassService) {
  }

  ngOnInit(): void {
    this.itemClasses$ = this.itemClassService.getAllItemClasses();
  }
}
