// Importar Axios
import axios from 'axios';
import { dataUtil } from './data-util.js'
import { getCurrentUser } from "@/services/local-storage.js";

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
    }
};

export default FriendService;