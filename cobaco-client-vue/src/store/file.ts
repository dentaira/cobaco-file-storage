import axios from 'axios';
import { Folder } from './folder.model';

export const downloadFile = async (fileId: string): Promise<[Blob, string]> => {
    // eslint-disable-next-line no-useless-catch
    try {
        const response = await axios.get('file/download/' + fileId, { responseType: 'blob' });
        const contentDisposition: string = response.headers['content-disposition'];
        return [new Blob([response.data]), decodeURI(contentDisposition.replace((/attachment;filename=(.*)/u), '$1'))];
    } catch (error) {
        throw error;
    }
}

export const uploadFile = async (file: File, parent: Folder, isRoot: boolean) => {
    let param = '';
    if (!isRoot) {
        param = '?parentId=' + parent!.fileId;
    }
    const form = new FormData();
    form.append('uploadFile', file);
    // eslint-disable-next-line no-useless-catch
    try {
        const response = await axios.post('file/upload' + param, form);
    } catch (error) {
        throw error;
    }
}

export const deleteFile = async (fileId: string) => {
    // eslint-disable-next-line no-useless-catch
    try {
        await axios.post('file/delete/' + fileId);
    } catch (error) {
        throw error;
    }
}