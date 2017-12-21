///<reference path="../../node_modules/@angular/core/src/metadata/lifecycle_hooks.d.ts"/>
import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {ConfigRef} from "./config-ref.class";


@Injectable()
export class FileRestService {
  cachedFiles: FileItem[];

  constructor(private http: HttpClient, private config: ConfigRef) {
    console.log("ngOnInit");
    this.reloadCache();
    var that = this;
    setInterval(() => {
      that.reloadCache();
    }, 2000);
  }


  getFiles() {
    return this.cachedFiles;
  }

  private reloadCache() {
    this.http
      .get<any>(this.config.nativeConfig.apiUrl + '/api/file')
      .subscribe(res => {
        console.log("REST called");
        if (!this.tablesEqual(res, this.cachedFiles)) {
          this.cachedFiles = res;
        } else {
          console.log("No change in results");
          console.log("\t current: " + JSON.stringify(res));
          console.log("\t cached : " + JSON.stringify(this.cachedFiles));
        }
      });


  }

  private tablesEqual(array1: any[], array2: any[]) {
    // if the other array2 is a falsy value, return
    if (!array2)
      return false;

    // compare lengths - can save a lot of time
    if (array1.length != array2.length)
      return false;

    for (var i = 0, l = array1.length; i < l; i++) {
      // Check if we have nested arrays
      if (array1[i] instanceof Array && array2[i] instanceof Array) {
        // recurse into the nested arrays
        if (!this.tablesEqual(array1[i], array2[i])) {
          return false;
        }
      }
      else if (array1[i] != array2[i]) {
        // Warning - two different object instances will never be equal: {x:20} != {x:20}
        return false;
      }
    }
    return true;
  }

}
