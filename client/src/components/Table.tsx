import { Row } from './Row'
import SubtitlesModel from "../model/SubtitlesModel";

interface IProps {
    subs: Array<SubtitlesModel>
}

export const Table: React.FC<IProps> = ({subs}) => {
    return (
        <table>
            <thead>
            <tr>
                <th key="name">FILE</th>
                <th key="host">HOST</th>
                <th key="size">SIZE</th>
                <th key="ext">EXT</th>
                <th key="dwn">DOWNLOAD</th>
            </tr>
            </thead>
            {subs ? (
                <tbody>
                {subs.map((sub, index) =>
                    <tr key={index}>
                        <Row subtitles={sub}/>
                    </tr>
                )}
                </tbody>
            ) : null}
        </table>
    )
}