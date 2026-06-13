export interface Comment {
    id: number
    author: {
        id: number
        name: string
        avatar?: string
    }
    text: string
    createdAt: string
    replies?: Comment[]
}