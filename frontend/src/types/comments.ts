export interface Comment {
    id: number
    author: {
        name: string
        avatar?: string
    }
    text: string
    createdAt: string
    replies?: Comment[]
}