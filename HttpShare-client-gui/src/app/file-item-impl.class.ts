export class FileItemImpl implements FileItem {
  id: string;
  name: string;
  removable: boolean;
  persistentDownload: boolean;
  url: string;

  constructor(id: string, name: string, removable: boolean, persistentDownload: boolean, url: string) {
    this.id = id;
    this.name = name;
    this.removable = removable;
    this.persistentDownload = persistentDownload;
    this.url = url;
  }
}
