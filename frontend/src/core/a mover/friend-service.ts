// Importar Axios
import axios, { AxiosResponse } from 'axios';
import { dataUtil } from './data-util';
import { getCurrentUser } from "@/core/services/local-storage";
import HttpClient from "@/core/services/HttpClient";

interface GetFriendsParams {
    userId: string;
    page?: number;
    pageSize?: number;
}

interface FriendServiceResponse {
    response: {
        content: any[]; // ajusta esto según el tipo de contenido
    };
}

const currentUser = getCurrentUser();
const currentUrl = dataUtil.CURRENT_URL;

// Definir el servicio de seguimiento
const FriendService = {
    async sendRequestFriendship(userIdTarget: string): Promise<any> {
        try {
            const response: AxiosResponse<any> = await axios.post(
                `${currentUrl}friend/createRequest/${userIdTarget}`,
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
            console.error('Error in send friend request:', error.message);
            throw new Error('Error in send friend request: ' + error.message);
        }
    },

    async cancelSendRequestFriendship(userIdTarget: string): Promise<any> {
        try {
            const response: AxiosResponse<any> = await axios.post(
                `${currentUrl}friend/cancelRequest/${userIdTarget}`,
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
            console.error('Error in cancel friend request:', error.message);
            throw new Error('Error in cancel friend request: ' + error.message);
        }
    },

    async cancelReceivedRequestFriendship(userIdTarget: string): Promise<any> {
        try {
            const response: AxiosResponse<any> = await axios.post(
                `${currentUrl}friend/cancelReceivedRequest/${userIdTarget}`,
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
            console.error('Error in cancel received friend request:', error.message);
            throw new Error('Error in cancel received friend request: ' + error.message);
        }
    },

    async acceptReceivedRequestFriendship(userIdTarget: string): Promise<any> {
        try {
            const response: AxiosResponse<any> = await axios.post(
                `${currentUrl}friend/acceptedRequest/${userIdTarget}`,
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
            console.error('Error in accept received friend request:', error.message);
            throw new Error('Error in accept received friend request: ' + error.message);
        }
    },

    async cancelFriendship(userIdTarget: string): Promise<any> {
        try {
            const response: AxiosResponse<any> = await axios.post(
                `${currentUrl}friend/deleteFriendship/${userIdTarget}`,
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
            console.error('Error in delete friendship:', error.message);
            throw new Error('Error in delete friendship: ' + error.message);
        }
    },

    async getFriends({ userId, page = 0, pageSize = 12 }: GetFriendsParams): Promise<any[]> {
        const config = { params: { page, size: pageSize } };

        try {
            const response: FriendServiceResponse = await HttpClient.get(
                `${currentUrl}friend/friendsOf/${userId}`,
                config,
            );

            return response?.response?.content ?? [];
        } catch (error: any) {
            console.error('Error in getting friends:', error.message);
            throw new Error('Error in getting friends: ' + error.message);
        }
    }
};

export default FriendService;
