export interface EpsonEposPlugin {
  echo(options: { value: string }): Promise<{ value: string }>;
}
