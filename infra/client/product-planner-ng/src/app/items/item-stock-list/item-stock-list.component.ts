import {Component} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {ItemStockHeader} from '../../shared/services/stock.service';
import {DataSource} from '@angular/cdk/collections';
import {Observable, of} from 'rxjs';

@Component({
  selector: 'app-item-stock-list',
  templateUrl: './item-stock-list.component.html',
  styleUrls: ['./item-stock-list.component.scss']
})
export class ItemStockListComponent {

  itemStockList: Array<ItemStockHeader>;
  datasource: DataSource<ItemStockHeader>;
  readonly displayedColumns = ['name', 'manufacturersCode', 'amount', 'unit'];

  constructor(private readonly activatedRoute: ActivatedRoute) {
    this.itemStockList = activatedRoute.snapshot.data.itemStockList;
    this.datasource = new ItemStockDatasource(this.itemStockList);
  }
}

export class ItemStockDatasource implements DataSource<ItemStockHeader> {

  constructor(private readonly data: Array<ItemStockHeader>) {
  }

  connect(): Observable<Array<ItemStockHeader>> {
    return of(this.data);
  }

  disconnect(): void {
  }

}
