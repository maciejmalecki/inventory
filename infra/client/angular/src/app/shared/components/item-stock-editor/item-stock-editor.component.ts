import {Component, Input, OnDestroy, OnInit} from '@angular/core';
import {Stock, StockService} from '../../services/stock.service';
import {Item} from '../../services/item.service';
import {ItemClass} from '../../services/item-class.service';
import {FormControl, FormGroup} from '@angular/forms';
import {Observable, Subject} from 'rxjs';
import {takeUntil} from 'rxjs/operators';

@Component({
  selector: 'app-item-stock-editor',
  templateUrl: './item-stock-editor.component.html',
  styleUrls: ['./item-stock-editor.component.scss']
})
export class ItemStockEditorComponent implements OnInit, OnDestroy {

  @Input()
  item: Item;
  @Input()
  itemClass: ItemClass;

  formGroup: FormGroup;
  changeAmountFormControl: FormControl;
  stock$: Observable<Stock>;

  private unsubscribe = new Subject();

  constructor(private readonly stockService: StockService) {
  }

  ngOnInit(): void {
    this.changeAmountFormControl = new FormControl('');
    this.formGroup = new FormGroup({
      changeAmount: this.changeAmountFormControl
    });
    this.stock$ = this.stockService.getStock(this.item.id)
      .pipe(takeUntil(this.unsubscribe));
  }

  ngOnDestroy(): void {
    this.unsubscribe.next();
    this.unsubscribe.complete();
  }

  replenish(): void {
    const amount = this.changeAmountFormControl.value;
    this.stock$ = this.stockService.replenish(this.item.id, amount)
      .pipe(takeUntil(this.unsubscribe));
    this.resetStockQuantity();
  }

  deduct(): void {
    const amount = this.changeAmountFormControl.value;
    this.stock$ = this.stockService.deduct(this.item.id, amount)
      .pipe(takeUntil(this.unsubscribe));
    this.resetStockQuantity();
  }

  private resetStockQuantity(): void {
    this.changeAmountFormControl.reset();
  }
}
