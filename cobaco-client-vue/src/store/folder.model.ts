import { File } from '@/store/file.model';

export interface Folder {
    fileId: string,
    name: string,
    ancestorsId: string[],
    ancestorsName: string[],
    children: File[],
}