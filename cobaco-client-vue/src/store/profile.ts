import { Profile } from "@/store/profile.model";
import { reactive } from "@vue/reactivity";

export const profileMockData: Profile = {
  userId: "66006b29-727e-4ed8-a3c8-95d4438f66d4",
  userName: "namihei",
  email: "local@domain.com",
  role: "user",
};

export const profileStore = reactive({ profile: null as Profile | null });
