import type { Comment } from '@/types/comments'

export const findCommentById = (comments: Comment[], id: number): Comment | undefined => {
    for (const comment of comments) {
        if (comment.id === id) return comment

        if (comment.replies && comment.replies.length > 0) {
            const found = findCommentById(comment.replies, id)
            if (found) return found
        }
    }
    return undefined
}

export const addCommentToTree = (
    commentsTree: Comment[],
    savedComment: any,
    authorName: string,
    parentId?: number
) => {
    const newComment: Comment = {
        id: savedComment.id,
        author: { name: authorName },
        text: savedComment.message,
        createdAt: savedComment.createdAt,
        replies: []
    }

    if (parentId) {
        const parentComment = findCommentById(commentsTree, parentId)
        if (parentComment) {
            if (!parentComment.replies) parentComment.replies = []
            parentComment.replies.push(newComment)
        }
    } else {
        commentsTree.unshift(newComment)
    }
}

export const buildCommentTree = (flatComments: any[]): Comment[] => {
    const commentMap = new Map<number, Comment>()
    const rootComments: Comment[] = []

    flatComments.forEach((c: any) => {
        const mappedComment: Comment = {
            id: c.id,
            author: {
                id: c.userId,
                name: c.authorName || `Пользователь ${c.userId}`
            },
            text: c.message,
            createdAt: c.createdAt, // Форматировать дату будем уже в шаблоне HTML!
            replies: []
        }

        commentMap.set(c.id, mappedComment)

        if (c.replyId) {
            commentMap.get(c.replyId)?.replies?.push(mappedComment)
        } else {
            rootComments.push(mappedComment)
        }
    })

    return rootComments.reverse()
}

export const deleteCommentFromTree = (comments: Comment[], targetId: number): boolean => {
    for (let i = 0; i < comments.length; i++) {
        if (comments[i].id === targetId) {
            comments.splice(i, 1)
            return true
        }
        if (comments[i].replies && comments[i].replies.length > 0) {
            const isDeleted = deleteCommentFromTree(comments[i].replies, targetId)
            if (isDeleted) return true
        }
    }
    return false
}