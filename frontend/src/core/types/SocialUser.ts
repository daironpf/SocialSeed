export interface SocialUser {
    id: string;
    dateBorn: string; // ISO 8601 string representation of LocalDateTime
    registrationDate: string; // ISO 8601 string representation of LocalDateTime
    fullName: string;
    userName: string;
    email: string;
    language: string;
    profileImage: string;
    bio: string;
    onVacation: boolean;
    isActive: boolean;
    isDeleted: boolean;
    friendCount: number;
    followersCount: number;
    followingCount: number;
    friendRequestCount: number;
}