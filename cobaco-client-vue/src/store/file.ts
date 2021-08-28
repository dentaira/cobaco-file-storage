import axios from 'axios';

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