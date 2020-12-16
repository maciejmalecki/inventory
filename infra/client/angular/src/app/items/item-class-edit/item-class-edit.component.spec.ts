import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ItemClassEditComponent } from './item-class-edit.component';

describe('ItemClassEditComponent', () => {
  let component: ItemClassEditComponent;
  let fixture: ComponentFixture<ItemClassEditComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ItemClassEditComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ItemClassEditComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
