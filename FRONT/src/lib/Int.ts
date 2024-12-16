export interface User {
    login: string;
    active: boolean;
    id: string;
    type: string;
}

export interface CreateUser {
    login: string;
    password: string;
    type: string;
}

export interface Rent {
    startDate: Date;
    endDate: Date;
    userID: string;
    workerId: string;
    id: string;
}