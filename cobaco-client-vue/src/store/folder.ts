import { Folder } from '@/store/folder.model';
import { reactive } from "@vue/reactivity";
import axios from 'axios';

export const fetchFolder = async (fileId: string): Promise<Folder> => {
    // eslint-disable-next-line no-useless-catch
    try {
        const response = await axios.get<Folder>('folder/' + fileId);
        return response.data;
    } catch (error) {
        throw error;
    }
}

export const fetchRoot = async (): Promise<Folder> => {
        // eslint-disable-next-line no-useless-catch
        try {
            const response = await axios.get<Folder>('folder/root/');
            return response.data;
        } catch (error) {
            throw error;
        }
}

export const createFolder = async (name: string, parent: Folder, isRoot: boolean): Promise<Folder> => {
    let param = '';
    if (!isRoot) {
        param = '?parentId=' + parent!.fileId;
    }
    // eslint-disable-next-line no-useless-catch
    try {
        const response = await axios.post<Folder>('folder/create/' + name + '/' + param);
        return response.data;
    } catch (error) {
        throw error;
    }
}