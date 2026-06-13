export interface PanelFieldOption {
    value: string | number;
    label: string;
}

export interface PanelFieldConfig {
    key: string;
    label: string;
    type?: 'text' | 'number' | 'date' | 'progress-bar' | 'select'; // можно расширять
    options?: PanelFieldOption[];
    from?: string;
    to?: string;
    readonly?: boolean;
}