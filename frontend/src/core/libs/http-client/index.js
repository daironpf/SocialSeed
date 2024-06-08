import axios from "axios";
import {getCurrentUser} from "@/core/services/local-storage.js";

const HttpClient = {
    async get(url, config) {
        const currentUser = getCurrentUser();

        Object.assign(config, {
            headers: {
                "Content-Type": "application/json",
                userId: currentUser?.id,
                ...config.headers,
            },
        });

        try {
            const response = await axios.get(url, config);

            return response.data;
        } catch (error) {
            console.error(`ERROR [HTTP REQUEST] GET ${url}`, {error: error.message});
            throw error;
        }
    }
};

export default HttpClient;