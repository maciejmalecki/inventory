import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {
  Attribute,
  isDictionaryType,
  isScalarType,
  ItemClass,
  ItemClassService
} from '../../shared/services/item-class.service';
import {FormControl, FormGroup} from '@angular/forms';
import {AttributeTypeHeader} from '../../shared/services/attribute-type.service';
import {CdkDragDrop, transferArrayItem} from '@angular/cdk/drag-drop';

function sub(left: Array<AttributeTypeHeader>, right: Array<AttributeTypeHeader>): Array<AttributeTypeHeader> {
  return left.filter(lValue => !!!right.find(rValue => rValue.name === lValue.name));
}

function mapToHeader(attribute: Attribute): AttributeTypeHeader {
  return {
    name: attribute.name,
    scalar: isScalarType(attribute.type),
    unitCode: isScalarType(attribute.type) ? attribute.type.unit.code : null,
    unitName: isScalarType(attribute.type) ? attribute.type.unit.name : null
  }
}

@Component({
  selector: 'app-item-class-edit',
  templateUrl: './item-class-edit.component.html',
  styleUrls: ['./item-class-edit.component.scss']
})
export class ItemClassEditComponent implements OnInit {

  itemClass: ItemClass;
  attributeTypes: Array<AttributeTypeHeader>;
  isScalarType = isScalarType;
  isDictionaryType = isDictionaryType;
  formGroup: FormGroup;
  selectedTypes: Array<AttributeTypeHeader>;
  unselectedTypes: Array<AttributeTypeHeader>;

  private descriptionControl: FormControl;

  constructor(
    private readonly route: ActivatedRoute,
    private readonly router: Router,
    private readonly itemClassService: ItemClassService) {

    this.itemClass = route.snapshot.data.itemClass;
    this.attributeTypes = route.snapshot.data.attributeTypes;
    this.selectedTypes = this.itemClass.attributes.map(mapToHeader);
    this.unselectedTypes = sub(this.attributeTypes, this.selectedTypes);
  }

  ngOnInit(): void {
    this.descriptionControl = new FormControl(this.itemClass.description, []);
    this.formGroup = new FormGroup({
      description: this.descriptionControl
    });
  }

  deleteDraft(): void {
    this.itemClassService.rejectDraftItemClass(this.itemClass.name).toPromise()
      .then(_ => this.router.navigate(['itemClasses', this.itemClass.name]));
  }

  save(): void {
    this.itemClassService.updateDraftItemClass(
      this.itemClass.name,
      this.descriptionControl.dirty ? this.descriptionControl.value : null,
      null,
      sub(this.selectedTypes, this.itemClass.attributes.map(mapToHeader)).map(value => value.name),
      sub(this.itemClass.attributes.map(mapToHeader), this.selectedTypes).map(value => value.name)
    ).toPromise()
      .then(_ => this.router.navigate(['itemClasses', this.itemClass.name]));
  }

  drop($event: CdkDragDrop<Array<AttributeTypeHeader>>): void {
    if ($event.previousContainer !== $event.container) {
      transferArrayItem($event.previousContainer.data,
        $event.container.data,
        $event.previousIndex,
        $event.currentIndex);
    }
  }
}
