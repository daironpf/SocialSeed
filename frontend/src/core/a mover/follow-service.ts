// Importar Axios
import axios from 'axios';
import { dataUtil } from './data-util';
import { getCurrentUser } from "@/core/services/local-storage";
import HttpClient from "@/core/services/HttpClient";
import { AxiosResponse } from 'axios';

interface FollowServiceResponse {
    response: {
        content: any[]; // ajusta esto según el tipo de contenido
    };
}

interface GetFollowsParams {
    userId: string;
    page?: number;
    pageSize?: number;
}

const currentUser = getCurrentUser();
const currentUrl = dataUtil.CURRENT_URL;

// Definir el servicio de seguimiento
const FollowService = {
    // Método para seguir a un usuario
    async followUser(userIdTarget: string): Promise<any> {
        try {
            const response: AxiosResponse<any> = await axios.post(
                `${currentUrl}follow/follow/${userIdTarget}`,
                null, // No data in the body
                {
                    headers: {
                        userId: currentUser?.id ?? ''
                    }
                }
            );

            if (response.status === 200) {
                return response.data;
            }
        } catch (error: any) {
            console.error('Error in following user:', error.message);
            throw new Error('Error in following user: ' + error.message);
        }
    },

    // Método para dejar de seguir a un usuario
    async unfollowUser(userIdTarget: string): Promise<any> {
        try {
            const response: AxiosResponse<any> = await axios.post(
                `${currentUrl}follow/unfollow/${userIdTarget}`,
                null, // No data in the body
                {
                    headers: {
                        userId: currentUser?.id ?? ''
                    }
                }
            );

            if (response.status === 200) {
                return response.data;
            }
        } catch (error: any) {
            console.error('Error in unfollowing user:', error.message);
            throw new Error('Error in unfollowing user: ' + error.message);
        }
    },

    // Método para obtener los usuarios que sigue
    async getFollows({ userId, page = 0, pageSize = 12 }: GetFollowsParams): Promise<any[]> {
        const config = { params: { page, size: pageSize } };

        try {
            const response: FollowServiceResponse = await HttpClient.get(
                `${currentUrl}follow/following/${userId}`,
                config
            );
            return response?.response?.content ?? [];
        } catch (error: any) {
            console.error('Error in getting follows:', error.message);
            throw new Error('Error in getting follows: ' + error.message);
        }
    },

    // Método para obtener los seguidores
    async getFollowers({ userId, page = 0, pageSize = 12 }: GetFollowsParams): Promise<any[]> {
        const config = { params: { page, size: pageSize } };

        try {
            const response: FollowServiceResponse = await HttpClient.get(
                `${currentUrl}follow/followers/${userId}`,
                config
            );
            return response?.response?.content ?? [];
        } catch (error: any) {
            console.error('Error in getting followers:', error.message);
            throw new Error('Error in getting followers: ' + error.message);
        }
    }
};

// Exportar el servicio
export default FollowService;
