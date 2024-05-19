// Importar Axios
import axios from 'axios';
import {dataUtil} from './data-util.js'
import {getCurrentUser} from "@/services/local-storage.js";
import HttpClient from "@/libs/http-client";

let currentUser = getCurrentUser();
let currentUrl = dataUtil.CURRENT_URL;

// Definir el servicio de seguimiento
const FriendService = {
    async sendRequestFriendship(userIdTarget) {
        try {
            const response = await axios.post(
                `${currentUrl}friend/createRequest/${userIdTarget}`,
                null, // No data in the body
                {
                    headers: {
                        userId: currentUser.id
                    }
                }
            );

            if (response.status === 200) {
                return response.data;
            }
        } catch (error) {
            console.error('Error in send friend request:', error.message);
        }
    },
    async cancelSendRequestFriendship(userIdTarget) {
        try {
            const response = await axios.post(
                `${currentUrl}friend/cancelRequest/${userIdTarget}`,
                null, // No data in the body
                {
                    headers: {
                        userId: currentUser.id
                    }
                }
            );

            if (response.status === 200) {
                return response.data;
            }
        } catch (error) {
            console.error('Error in cancel friend request:', error.message);
        }
    },
    async cancelReceivedRequestFriendship(userIdTarget) {
        try {
            const response = await axios.post(
                `${currentUrl}friend/cancelReceivedRequest/${userIdTarget}`,
                null, // No data in the body
                {
                    headers: {
                        userId: currentUser.id
                    }
                }
            );

            if (response.status === 200) {
                return response.data;
            }
        } catch (error) {
            console.error('Error in cancel received friend request:', error.message);
        }
    },
    async acceptReceivedRequestFriendship(userIdTarget) {
        try {
            const response = await axios.post(
                `${currentUrl}friend/acceptedRequest/${userIdTarget}`,
                null, // No data in the body
                {
                    headers: {
                        userId: currentUser.id
                    }
                }
            );

            if (response.status === 200) {
                return response.data;
            }
        } catch (error) {
            console.error('Error in cancel received friend request:', error.message);
        }
    },
    async cancelFriendship(userIdTarget) {
        try {
            const response = await axios.post(
                `${currentUrl}friend/deleteFriendship/${userIdTarget}`,
                null, // No data in the body
                {
                    headers: {
                        userId: currentUser.id
                    }
                }
            );

            if (response.status === 200) {
                return response.data;
            }
        } catch (error) {
            console.error('Error in delete friendship:', error.message);
        }
    },

    async getFriends({userId, currentPage = 0, pageSize = 12}) {
        const config = { params: {page: currentPage, size: pageSize } };

        const response = await HttpClient.get(
            `${currentUrl}friend/friendsOf/${userId}`,
            config,
        );

        return response?.response?.content ?? [];
    }
};

export default FriendService;