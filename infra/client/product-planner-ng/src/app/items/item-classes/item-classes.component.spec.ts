import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ItemClassesComponent } from './item-classes.component';

describe('ItemClassesComponent', () => {
  let component: ItemClassesComponent;
  let fixture: ComponentFixture<ItemClassesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ItemClassesComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ItemClassesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
