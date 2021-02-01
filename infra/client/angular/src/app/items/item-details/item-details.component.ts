import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {DictionaryValue, isDictionaryValue, isScalarValue, Item, ItemService} from '../../shared/services/item.service';
import {Stock} from '../../shared/services/stock.service';

@Component({
  selector: 'app-item-details',
  templateUrl: './item-details.component.html',
  styleUrls: ['./item-details.component.scss']
})
export class ItemDetailsComponent implements OnInit {

  item: Item;
  stock: Stock;
  isScalarValue = isScalarValue;
  isDictionaryValue = isDictionaryValue;

  constructor(
    private readonly activatedRoute: ActivatedRoute,
    private readonly router: Router,
    private readonly itemService: ItemService
  ) {
    this.item = activatedRoute.snapshot.data.item;
    this.stock = activatedRoute.snapshot.data.stock;
  }

  ngOnInit(): void {
  }

  getDictionaryValue(value: DictionaryValue): string {
    return value.items.find(value1 => value1.code === value.value).value;
  }

  delete(): void {
    this.itemService.deleteItem(this.item.name).subscribe(value => {
      if (value.ok) {
        this.router.navigate(['items']).catch(reason => console.log(reason));
      }
    });
  }
}
