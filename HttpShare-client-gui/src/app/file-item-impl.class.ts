export class FileItemImpl implements FileItem {
  id: string;
  removable: boolean;
  persistentDownload: boolean;
  url: string;

  constructor(id: string, removable: boolean, persistentDownload: boolean, url: string) {
    this.id = id;
    this.removable = removable;
    this.persistentDownload = persistentDownload;
    this.url = url;
  }
}
