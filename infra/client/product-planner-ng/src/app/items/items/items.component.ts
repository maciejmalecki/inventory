import {Component, OnInit} from '@angular/core';
import {Observable} from 'rxjs';
import {ItemHeader, ItemService} from '../../shared/services/item.service';

@Component({
  selector: 'app-items',
  templateUrl: './items.component.html',
  styleUrls: ['./items.component.scss']
})
export class ItemsComponent implements OnInit {

  constructor(private readonly itemService: ItemService) {
  }

  items$: Observable<Array<ItemHeader>>;

  ngOnInit(): void {
    this.items$ = this.itemService.getAllItems();
  }
}
