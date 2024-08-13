import axios, {AxiosRequestConfig, AxiosResponse} from "axios";
import {getCurrentUser} from "@/core/services/local-storage";
import {SocialUser} from "@/core/types/SocialUser";


const HttpClient = {
    async get<T>(url: string, config: AxiosRequestConfig = {}): Promise<T> {
        const currentUser: SocialUser | null = getCurrentUser();

        config.headers = {
            "Content-Type": "application/json",
            userId: currentUser?.id,
            ...config.headers,
        };

        try {
            const response: AxiosResponse<T> = await axios.get<T>(url, config);

            return response.data;
        } catch (error: any) {
            console.error(`ERROR [HTTP REQUEST] GET ${url}`, {error: error.message});
            throw error;
        }
    },
};

export default HttpClient;