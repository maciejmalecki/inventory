import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ItemStockListComponent } from './item-stock-list.component';

describe('ItemStockListComponent', () => {
  let component: ItemStockListComponent;
  let fixture: ComponentFixture<ItemStockListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ItemStockListComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ItemStockListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
