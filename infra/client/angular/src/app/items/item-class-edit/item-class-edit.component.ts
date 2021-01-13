import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {isDictionaryType, isScalarType, ItemClass, ItemClassService} from '../../shared/services/item-class.service';
import {FormControl, FormGroup} from '@angular/forms';
import {AttributeTypeHeader} from '../../shared/services/attribute-type.service';

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

  constructor(
    private readonly route: ActivatedRoute,
    private readonly router: Router,
    private readonly itemClassService: ItemClassService) {

    this.itemClass = route.snapshot.data.itemClass;
    this.attributeTypes = route.snapshot.data.attributeTypes;
    this.selectedTypes = this.itemClass.attributes.map(value => ({name: value.name, scalar: isScalarType(value.type)}));
    this.unselectedTypes = [...this.attributeTypes];
  }

  ngOnInit(): void {
    this.formGroup = new FormGroup({
      description: new FormControl(this.itemClass.description, [])
    });
  }

  deleteDraft(): void {
    this.itemClassService.rejectDraftItemClass(this.itemClass.name).toPromise()
      .then(_ => this.router.navigate(['itemClasses', this.itemClass.name]));
  }
}
