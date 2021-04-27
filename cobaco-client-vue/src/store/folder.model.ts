import { File } from '@/store/file.model';

export interface Folder {
    fileId: string,
    name: string,
    children: File[],
    ancestorIds: string[],
    ancestorNames: string[],
}