export interface Task {
    id: number;
    projectId: number;
    parentTaskId: number | null;
    authorId: number;
    statusId: number;
    priorityId: number;
    name: string;
    description: string | null;
    deadlineDate: string | null;
    completedAt: string | null;
    createdAt: string;
    updatedAt: string;
}
