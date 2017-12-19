import {Injectable} from '@angular/core';
import {FileItemImpl} from "./file-item-impl.class";


@Injectable()
export class FileRestService {
  constructor() {
  }

  getFiles() {
    var files: FileItem[] = [new FileItemImpl('ala', false, false, 'ala'),
      new FileItemImpl('ma', false, false, 'ma'),
      new FileItemImpl('kota', false, false, 'kota'),
      new FileItemImpl('i', false, false, 'i'),
      new FileItemImpl('kot', false, false, 'kot'),
      new FileItemImpl('ma', false, false, 'ma'),
      new FileItemImpl('ale', false, false, 'ale')];

    return files;

  }
}
