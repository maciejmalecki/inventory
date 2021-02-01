import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ItemStockEditorComponent } from './item-stock-editor.component';

describe('ItemStockEditorComponent', () => {
  let component: ItemStockEditorComponent;
  let fixture: ComponentFixture<ItemStockEditorComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ItemStockEditorComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ItemStockEditorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
