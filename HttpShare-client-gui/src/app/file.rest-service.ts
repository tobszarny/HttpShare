///<reference path="../../node_modules/@angular/core/src/metadata/lifecycle_hooks.d.ts"/>
import {Injectable, OnInit} from '@angular/core';
import {FileItemImpl} from "./file-item-impl.class";
import {HttpClient} from '@angular/common/http';
import {ConfigRef} from "./config-ref.class";


@Injectable()
export class FileRestService implements OnInit {
  ngOnInit(): void {
    this.http
      .get<FileItem[]>(this.config.nativeConfig.apiUrl + '/api/file')
      .subscribe(
        data => {
          console.log(data);
        }
      );
  }

  constructor(private http: HttpClient, private config: ConfigRef) {

  }

  getFiles() {

    console.log("dada");
    // this.http.get(this.config.nativeConfig.apiUrl+'/api/file');

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
