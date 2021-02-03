import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ItemClassDetailsComponent } from './item-class-details.component';

describe('ItemClassDetailsComponent', () => {
  let component: ItemClassDetailsComponent;
  let fixture: ComponentFixture<ItemClassDetailsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ItemClassDetailsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ItemClassDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
