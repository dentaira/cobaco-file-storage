import { Profile } from "@/store/profile.model";
import { reactive } from "@vue/reactivity";
import axios, { AxiosBasicCredentials } from 'axios';

export const profileStore = reactive({ 
  profile: null as Profile | null
 });

export const signInAsync = async (username: string, password: string): Promise<Profile> => {
  const credentials: AxiosBasicCredentials = {
    username: username,
    password: password
  };

  // eslint-disable-next-line no-useless-catch
  try {
    const response = await axios.post<Profile>('sign-in/account', null, { auth: credentials });
    axios.defaults.auth = credentials;
    return response.data;
  } catch (error) {
    throw error;
  }
}

export const signOutAsync = async () => {
  profileStore.profile = null;
  axios.defaults.auth = undefined;
}