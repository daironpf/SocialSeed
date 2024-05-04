// Importar Axios
import axios from 'axios';
import { dataUtil } from './data-util.js'
import { getCurrentUser } from "@/services/local-storage.js";

let currentUser = getCurrentUser();
let currentUrl = dataUtil.CURRENT_URL;

// Definir el servicio de seguimiento
const FollowService = {
    // Método para dejar de seguir a un usuario
    async followUser(userIdTarget) {
        try {
            const response = await axios.post(
                `${currentUrl}follow/follow/${userIdTarget}`,
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
            console.error('Error in following user:', error.message);
        }
    },
    async unfollowUser(userIdTarget) {
        try {
            const response = await axios.post(
                `${currentUrl}follow/unfollow/${userIdTarget}`,
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
            throw new Error('Error in unfollowing user: ' + error.message);
        }
    }
};

// Exportar el servicio
export default FollowService;