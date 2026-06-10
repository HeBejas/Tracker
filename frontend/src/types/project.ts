export interface Project {
    id: number;
    workspaceId: number;
    statusId: number;
    name: string;
    description: string | null;
    startDate: string | null;
    deadlineDate: string | null;
    completedAt: string | null;
    totalTasksCount: number;
    completedTasksCount: number;
    createdAt: string;
    updatedAt: string;
}