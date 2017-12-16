export class FileItemsImpl {
  items: FileItem[];

  constructor(items?: FileItem[]) {
    if (items) {
      this.items = items;
    }
  }
}
