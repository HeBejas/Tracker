export interface Task {
    id: number;
    projectId: number;
    projectName: string;
    parentTaskId: number | null;
    authorId: number;
    authorName: string;
    statusId: number;
    priorityId: number;
    name: string;
    description: string | null;
    deadlineDate: string | null;
    completedAt: string | null;
    createdAt: string;
    updatedAt: string;
}
