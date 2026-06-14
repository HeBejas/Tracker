export interface PanelFieldConfig {
    key: string
    label: string
    type?: 'text' | 'number' | 'date' | 'progress-bar' | 'select'
    options?: { value: any, label: string }[]
    readonly?: boolean
    sortable?: boolean
    filterable?: boolean
    to?: string
    from?: string
}