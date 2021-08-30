import { Folder } from '@/store/folder.model';
import { reactive } from "@vue/reactivity";
import axios from 'axios';

const folderMockData: Folder[] = [
    {
        fileId: '66006b29-727e-4ed8-a3c8-95d4438fd001',
        name: 'フォルダ1',
        ancestorIds: [],
        ancestorNames: [],
        children: [
            {
                fileId: '66006b29-727e-4ed8-a3c8-95d4438f00a1',
                name: 'ファイル1',
                type: 'FILE',
                size: 138,
            },
            {
                fileId: '66006b29-727e-4ed8-a3c8-95d4438f00a2',
                name: 'ファイル2',
                type: 'FILE',
                size: 982,
            },
            {
                fileId: '66006b29-727e-4ed8-a3c8-95d4438f00d2',
                name: 'フォルダ2',
                type: 'DIRECTORY',
                size: 0,
            },
        ],
    },
    {
        fileId: '66006b29-727e-4ed8-a3c8-95d4438f00d2',
        name: 'フォルダ2',
        ancestorIds: ['66006b29-727e-4ed8-a3c8-95d4438fd001'],
        ancestorNames: ['フォルダ1'],
        children: [
            {
                fileId: '66006b29-727e-4ed8-a3c8-95d4438f00b1',
                name: 'ファイル2-1',
                type: 'FILE',
                size: 148,
            },
        ],
    },
];

const homeFolder: Folder = {
    fileId: 'home',
    name: 'home',
    ancestorIds: [],
    ancestorNames: [],
    children: [
        {
            fileId: '66006b29-727e-4ed8-a3c8-95d4438fd001',
            name: 'フォルダ1',
            type: 'DIRECTORY',
            size: 0,
        }
    ]
}

export const folderStore = reactive({
    folders: folderMockData as Folder[],
    home: homeFolder as Folder,
    get: (id: string) => {
        return folderMockData.filter(folder => folder.fileId === id)[0];
    },
});

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